package fr.polytech.rimel.comclass.classifiers.helpers;

import org.repodriller.domain.DiffBlock;
import org.repodriller.domain.DiffLineType;
import org.repodriller.domain.DiffParser;
import org.repodriller.domain.Modification;

import java.util.function.Predicate;
import java.util.stream.Stream;

public class MatchingLinesInFile implements ClassificationHelper {

    private final Predicate<String> lineChecker;

    public MatchingLinesInFile(Predicate<String> lineChecker) {
        this.lineChecker = lineChecker;
    }

    @Override
    public long countMatchingLines(Modification modification) {
        DiffParser diff = new DiffParser(modification.getDiff());

        int commentsCount = 0;

        for (DiffBlock block : diff.getBlocks()) {
            commentsCount += Stream.concat(block.getLinesInOldFile().stream(), block.getLinesInNewFile().stream())
                    .filter(line -> line.getType() == DiffLineType.ADDED || line.getType() == DiffLineType.REMOVED)
                    .filter(line -> lineChecker.test(line.getLine()))
                    .count();
        }

        return commentsCount;
    }
}
