package fr.polytech.rimel.comclass.classifiers.helpers;

import org.repodriller.domain.Modification;

@FunctionalInterface
public interface ClassificationHelper {

    long countMatchingLines(Modification modification);
}
