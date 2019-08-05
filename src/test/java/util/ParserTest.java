package util;

import org.junit.Test;
import util.Parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ParserTest {

    private static final String BASE_PATH = "src/test/resources/";

    private final Parser parser = new Parser();

    @Test
    public void parseFile_WithInvalidFile_ThrowsParseException() {
        assertThatThrownBy(() -> parser.parseFile("invalid", ","))
                .hasMessage("File with path 'invalid' cannot be found.");
    }

    @Test
    public void parseFile_WithHeader_ReturnsValidParseResult() {
        var actual = parser.parseFile(BASE_PATH + "test-csv-header", ",");

        assertThat(actual.getUserRatings().size()).isEqualTo(3);
    }

    @Test
    public void parseFile_WithoutHeader_ReturnsValidParseResult() {
        var actual = parser.parseFile(BASE_PATH + "test-csv-no-header", ",");

        assertThat(actual.getUserRatings().size()).isEqualTo(3);
    }

    @Test
    public void parseFile_WithTabSeparatedFile_ReturnsValidParseResult() {
        var actual = parser.parseFile(BASE_PATH + "test-tsv-no-header", "\t");

        assertThat(actual.getUserRatings().size()).isEqualTo(3);
    }
}
