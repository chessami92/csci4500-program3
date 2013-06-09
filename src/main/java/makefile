JAVAC=javac
sources = $(wildcard **/*.java)
classes = $(sources:.java=.class)

all: $(sources)
	$(JAVAC) $(sources)
	
clean :
	rm -f **/*.class

