package org.folio;

import org.folio.converter.ParsedRecordDtoToQuickMarcConverter;
import org.folio.converter.QuickMarcToParsedRecordDtoConverter;
import org.folio.rest.jaxrs.model.QuickMarcJson;
import org.folio.srs.model.ParsedRecordDto;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.profile.StackProfiler;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

import static org.folio.Utils.getCurrentDateTimeAsString;
import static org.folio.Utils.getMockAsJson;

/**
 * Micro benchmark for QuickMarcJson <-> ParsedRecordDto converters.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
@Fork(1)
@Warmup(iterations = 10, time = 1)
@Measurement(iterations = 20, time = 1, batchSize = 1)
public class ConverterLoop {

    public static final String QUICK_MARC_JSON_FILE = "quick_marc_json.json";

    private ParsedRecordDtoToQuickMarcConverter parsedRecordToQuickMarcConverter;
    private QuickMarcToParsedRecordDtoConverter quickMarcToParsedRecordDtoConverter;
    private ParsedRecordDto parsedRecordDto;
    private QuickMarcJson quickMarcJson;

    public static void main(String[] args) throws RunnerException {

        Options options = new OptionsBuilder()
                .include(ConverterLoop.class.getSimpleName())
                .resultFormat(ResultFormatType.JSON)
                .result("benchmark-result/" + getCurrentDateTimeAsString() + ".json")
                .addProfiler(StackProfiler.class)
                .forks(1)
                .build();

        new Runner(options).run();
    }

    @Setup(Level.Trial)
    public void setup() {
        parsedRecordToQuickMarcConverter = new ParsedRecordDtoToQuickMarcConverter();
        quickMarcToParsedRecordDtoConverter = new QuickMarcToParsedRecordDtoConverter();
        quickMarcJson = getMockAsJson(QUICK_MARC_JSON_FILE).mapTo(QuickMarcJson.class);
        parsedRecordDto = quickMarcToParsedRecordDtoConverter.convert(quickMarcJson);
    }

    /**
     * Benchmark for ParsedRecordDto -> QuickMarcJson conversion
     */
    @Benchmark
    public void parsedRecordToQuickMarcJson() {
        parsedRecordToQuickMarcConverter.convert(parsedRecordDto);
    }

    /**
     * Benchmark for QuickMarcJson -> ParsedRecordDto conversion
     */
    @Benchmark
    public void quickMarcJsonToParsedRecordDto() {
        quickMarcToParsedRecordDtoConverter.convert(quickMarcJson);
    }
}
