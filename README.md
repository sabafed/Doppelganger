# Doppelganger

This Master's Degree project consists in reading and comparing glycosilation networks provided by the tool *Compozitor* hosted at the *GlyConnect* database on the *ExPASy* server of the *SIB* (Swiss Institute of Bioinformatics).

Doppelganger is designed to help researchers explore new options in their lab work by suggesting biologically relevant glycomes to the user.

To facilitate code maintenance and future modification, the project is subdivided in multiple packages by task, with the possibility to accommodate more:

- **downloader** package uses the HTTP protocol to retrieve glycome data from GlyConnect database through its API and saves them in JSON format under the dataAll directory.
- **doppelganger** package reads the data from the chosen dataAll subdirectory and creates a Java Object representation of them.
- **scorer** package elaborates doppelgangers' information in order to perform a pairwise comparison of glycomes and saves the results under the results directory.
- **QA** package analyses the results and produces statistics on the specificity and sensitivity of the methods utilised to compute the scores.


