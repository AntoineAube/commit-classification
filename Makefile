DATASETS_BUILDER = datasets-builder.jar
STUDIED_REPOSITORY = scikit-learn
REPOSITORY_URL = https://github.com/scikit-learn/scikit-learn.git
STUDY_RESULTS = study-results
NOTEBOOK_TITLE = study-report

clean:
	rm -f $(DATASETS_BUILDER) $(NOTEBOOK_TITLE).html

$(DATASETS_BUILDER):
	mvn -f datasets-builder/pom.xml clean package assembly:single
	mv datasets-builder/target/datasets-builder-1.0-SNAPSHOT-jar-with-dependencies.jar ./$(DATASETS_BUILDER)

$(STUDIED_REPOSITORY):
	git clone $(REPOSITORY_URL) $(STUDIED_REPOSITORY)

$(STUDY_RESULTS): datasets-builder.jar scikit-learn
	java -jar datasets-builder/target/datasets-builder-1.0-SNAPSHOT-jar-with-dependencies.jar -r scikit-learn/ -o $(STUDY_RESULTS)

study: $(STUDY_RESULTS)
	jupyter nbconvert $(NOTEBOOK_TITLE).ipynb
