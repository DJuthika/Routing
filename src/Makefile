JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES =  Arrange.java \
           Tries.java \
           Graph.java \
           ssp.java \
           Fibonacci.java \
           FibonacciHeapNode.java \
           routing.java \

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
