import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.assertj.core.api.Assertions.assertThat;

public class ZippedFilesLesson {

    private final ClassLoader cl = ZippedFilesLesson.class.getClassLoader();

    @Test
    void zipPdfTest() throws Exception {

        try (ZipFile zf = new ZipFile(new File(Objects.requireNonNull(cl.getResource("TestZipFile.zip")).toURI()))) {
            ZipEntry zePdf = zf.getEntry("TestFile.pdf");
            try (InputStream isPdf = zf.getInputStream(zePdf)) {
                PDF parsedPdf = new PDF(isPdf);

                assertThat(parsedPdf.text).contains("Правила Дорожного Движения");
                assertThat(parsedPdf.numberOfPages).isEqualTo(41);
            }

            ZipEntry zeCsv = zf.getEntry("TestFile.csv");
            try (InputStream isCsv = zf.getInputStream(zeCsv)) {
                CSVReader parsedCsv = new CSVReader(new InputStreamReader(isCsv));
                List<String[]> list = parsedCsv.readAll();

                assertThat(list)
                        .hasSize(3)
                        .contains(
                                new String[]{"test;test1"},
                                new String[]{"test2;test3"},
                                new String[]{"test4;test5"}
                        );
            }

            ZipEntry zeXls = zf.getEntry("TestFile.xlsx");
            try (InputStream isXls = zf.getInputStream(zeXls)){
                XLS parsedXlsx = new XLS(isXls);

                assertThat(parsedXlsx.excel.getSheetAt(0).getRow(1).getCell(1).getStringCellValue()).isEqualTo("Действия");
            }
        }
    }
}
