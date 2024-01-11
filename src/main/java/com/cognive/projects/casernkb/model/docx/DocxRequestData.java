package com.cognive.projects.casernkb.model.docx;

import com.prime.db.rnkb.model.Payment;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.poi.xwpf.usermodel.XWPFTable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class DocxRequestData {
    private static final DateTimeFormatter RUS = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Long id;
    private LocalDateTime date;
    private String legalName;
    private String inn;
    private String header;
    private List<Payment> payments;
    private String requestInfo;
    private String additionalInfo;
    private String conclusion;
    private boolean isIndividual;

    public static Map<String, String> getDataParams(DocxRequestData exportDto) {
        return Map.ofEntries(
                Map.entry("requestId", exportDto.getId().toString()),
                Map.entry("requestDate", exportDto.getDate().format(RUS)),
                Map.entry("requestOrg", exportDto.getLegalName()),
                Map.entry("requestInn", exportDto.getInn()),
                Map.entry("requestHeader", exportDto.getHeader()),
                Map.entry("requestInfo", exportDto.getRequestInfo()),
                Map.entry("requestAdditional", exportDto.getAdditionalInfo()),
                Map.entry("requestConclusion", exportDto.getConclusion())
        );
    }

    public static void setTableParams(DocxRequestData exportDto, XWPFTable xwpfTable) {
        exportDto.getPayments().forEach(p -> {
            var row = xwpfTable.createRow();

            var cell = row.getCell(0);
            var paragraph = cell.addParagraph();
            var run = paragraph.createRun();
            var date = p.getDocCreateDate() != null ? p.getDocCreateDate().format(RUS) : "-";
            run.setText(date + " № " + createText(p.getOperationNumber()));

            cell = row.getCell(1);
            paragraph = cell.addParagraph();
            run = paragraph.createRun();
            run.setText(createText(p.getPayeeName()));

            cell = row.getCell(2);
            paragraph = cell.addParagraph();
            run = paragraph.createRun();
            run.setText(createText(p.getPayeeInn()));

            cell = row.getCell(3);
            paragraph = cell.addParagraph();
            run = paragraph.createRun();
            run.setText(createText(p.getAmount().toString()));

            cell = row.getCell(4);
            paragraph = cell.addParagraph();
            run = paragraph.createRun();
            run.setText(createText(p.getPurpose()));
        });
    }

    private static String createText(String text) {
        return text != null ? text : "-";
    }
}
