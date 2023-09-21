package com.mindhub.homebanking.controllers;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.springframework.http.HttpHeaders;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;



import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class TransactionsController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @GetMapping("/transactions/generate-pdf")
    public ResponseEntity<Object> getTransactionsbyDateTime(@RequestParam String dateInit,
                                                            @RequestParam String dateEnd,
                                                            @RequestParam String accountNumber,
                                                            Authentication authentication) throws DocumentException, IOException {
        Client current = clientService.findByEmail(authentication.getName());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm");


        if (current == null) {
            return new ResponseEntity<>("you are not allowed to see this", HttpStatus.FORBIDDEN);
        }
        if (!accountService.existsByNumber(accountNumber)) {
            return new ResponseEntity<>("this account dont exist", HttpStatus.BAD_REQUEST);
        }
        if (dateInit.isBlank()) {
            return new ResponseEntity<>("Please, fill the date requeriment", HttpStatus.BAD_REQUEST);
        }
        if (dateEnd.isBlank()) {
            return new ResponseEntity<>("Please, fill the date end requeriment",HttpStatus.BAD_REQUEST);
        }
        if (dateInit.equals(dateEnd)) {
            return new ResponseEntity<>("You cant use the same date", HttpStatus.BAD_REQUEST);
        }

        LocalDateTime localDateTime = LocalDateTime.parse(dateInit, formatter);
        LocalDateTime localDateTime2 = LocalDateTime.parse(dateEnd, formatter);

        List<Transaction> transactions = transactionService.findByDateBetweenAndAccount_Number(localDateTime, localDateTime2, accountNumber);

        if (transactions.size() <= 0){
            return new ResponseEntity<>("No transactions finded.",HttpStatus.NOT_FOUND);
        }

        Document doc = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(doc, out);
        doc.open();

        PdfPTable tableTitle = new PdfPTable(1);
        PdfPCell cell = new PdfPCell();
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(10);
        cell.addElement(new Paragraph("Your transactions", new Font(Font.HELVETICA, 24)));
        tableTitle.addCell(cell);
        doc.add(tableTitle);

        PdfPTable logo = new PdfPTable(2);
        logo.setWidthPercentage(100);
        Image img = Image.getInstance("C:\\Users\\prisc\\OneDrive\\Escritorio\\PRUEBA PDF\\MindhubBrothers.png");
        img.scaleToFit(80, 60);
        img.setAbsolutePosition(50, 50);
        img.setAlignment(Image.ALIGN_BASELINE);
        PdfPCell imageCell = new PdfPCell(img);
        imageCell.setBorder(PdfPCell.NO_BORDER);
        logo.addCell(imageCell);
        PdfPCell textCell = new PdfPCell();
        textCell.setBorder(PdfPCell.NO_BORDER);
        textCell.addElement(new Phrase("MindHub Brothers Bank"));
        logo.addCell(textCell);


        PdfPTable table = new PdfPTable(5);
        table.addCell("Type");
        table.addCell("Description");
        table.addCell("Amount");
        table.addCell("Date");
        table.addCell("balance");

        for (Transaction transaction : transactions.stream().filter(Transaction::getActive).collect(Collectors.toSet())) {
            table.addCell(transaction.getType().toString());
            table.addCell(transaction.getDescription());
            table.addCell(String.valueOf(transaction.getAmount()));
            table.addCell(transaction.getDate().format(formatter));
            table.addCell(transaction.getCurrentBalance().toString());
        }
        doc.add(table);

        PdfPCell spacerCell = new PdfPCell();
        spacerCell.setFixedHeight(50);
        spacerCell.setBorder(PdfPCell.NO_BORDER);
        spacerCell.setColspan(4);
        doc.add(spacerCell);


        doc.add(logo);
        doc.close();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=transactions-Table.pdf");
        byte[] pdf = out.toByteArray();

        return new ResponseEntity<>(pdf,headers, HttpStatus.CREATED);
    }

    @Transactional
    @PostMapping("/api/transactions")
    public ResponseEntity<Object> createTransactions(

            @RequestParam double amount, @RequestParam String description,

            @RequestParam String numberOrigin, @RequestParam String numberDestiny, Authentication authentication) {

        if (authentication == null) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        if (amount <= 0) {

            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);

        }if (description.isBlank()){

            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);

        }if (numberOrigin.isBlank()){

            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);

        }if (numberDestiny.isBlank()){

            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }


        if (numberOrigin.equals(numberDestiny)) {
            return new ResponseEntity<>("They are the same accounts, they cannot be the same ", HttpStatus.FORBIDDEN);
        }

        Client client = clientService.findByEmail(authentication.getName());

        Account account = accountService.findByNumberAndClient(numberOrigin, client);

        Account accountDest = accountService.findByNumber(numberDestiny);


        if ( account == null ){
            return new ResponseEntity<>("source account does not exist", HttpStatus.FORBIDDEN);
        }

        if (accountDest == null){
            return new ResponseEntity<>("destination account does not exist", HttpStatus.FORBIDDEN);
        }

        if (account.getBalance() >= amount) {


            account.setBalance(account.getBalance() - amount);
            accountDest.setBalance(accountDest.getBalance() + amount);


            Transaction transactionDebit = new Transaction(TransactionType.DEBIT,  -amount, LocalDateTime.now(), description + " " + accountDest.getNumber(), account.getBalance()-amount,true);

            Transaction transactionCredit = new Transaction(TransactionType.CREDIT, amount, LocalDateTime.now(), description + " " + account.getNumber(), accountDest.getBalance()+amount,true);

            transactionService.transactionSave(transactionDebit);
            transactionService.transactionSave(transactionCredit);
            account.addTransaction(transactionDebit);
            accountDest.addTransaction(transactionCredit);
            accountService.addAccount(account);
            accountService.addAccount(accountDest);
        } else {

            return new ResponseEntity<>("there is not enough balance", HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>("the transfer was created", HttpStatus.CREATED);
    }

}
