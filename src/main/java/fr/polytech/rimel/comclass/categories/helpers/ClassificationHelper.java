package fr.polytech.rimel.comclass.categories.helpers;

import org.repodriller.domain.Modification;

@FunctionalInterface
public interface ClassificationHelper {

    long countMatchingLines(Modification modification);
}
