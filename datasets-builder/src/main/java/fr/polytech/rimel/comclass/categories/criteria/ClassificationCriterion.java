package fr.polytech.rimel.comclass.categories.criteria;

import org.repodriller.domain.Modification;

@FunctionalInterface
public interface ClassificationCriterion {

    long countMatchingLines(Modification modification);
}
