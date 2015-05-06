MAKEFLAGS = j4
.PHONY : compile

PROJ = $(PWD)

BIN_PATH = $(PROJ)/bin
SOURCE_PATH = $(PROJ)/src/main/java

CLASS_PATH = $(PROJ)/lib/jparsec-2.0.jar

RUN_FLAGS = -cp $(CLASS_PATH):$(BIN_PATH)
C_FLAGS = -d $(BIN_PATH) -cp $(CLASS_PATH) -sourcepath $(SOURCE_PATH) #-Xlint:all

compile:
	javac $(C_FLAGS) $(SOURCE_PATH)/com/zachmatt/irc/exceptions/*.java
	javac $(C_FLAGS) $(SOURCE_PATH)/com/zachmatt/irc/messages/*.java
	javac $(C_FLAGS) $(SOURCE_PATH)/com/zachmatt/irc/server/*.java
	javac $(C_FLAGS) $(SOURCE_PATH)/com/zachmatt/irc/client/*.java

runServer: compile
	java $(RUN_FLAGS) com.zachmatt.irc.server.Server

runClient: compile
	java $(RUN_FLAGS) com.zachmatt.irc.client.Client
