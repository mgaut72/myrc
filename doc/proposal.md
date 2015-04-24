MYRC
====

Teammates: Matt Gautreau and Zachary Montoya

Overview: MYRC is to be a basic chat server, conforming to a subset of the IRC
protocol outlined in [RFC 2812](https://tools.ietf.org/html/rfc2812).  The
program will manage connected clients, and facilitate server-client and
client-client interactions.

Application Requirements: The project will be focused on users interacting
with each other, so only a subset of the IRC protocol will be implemented.
Section 3 of the protocol defines the purpose and structure of each type of
message. This project will only be concerned with messages contained within
sections 3.1, 3.2, 3.3, 3.6, and 3.7, (Connection Registration, Channel
Operations, Sending messages, User based queries, and Miscellaneous messages).

Distributed Requirements: For our project, we intend to begin with one server
and at least two clients. The IRC protocol allows clients to send private
messages and to send messages to channels. Thus, the server will be running
multiple threads to handle the relaying of communication between clients and
over channels. Additionally, users may perform user identification commands and
they may send channel commands. Thus, the server must also keep track of
various information to be able to respond to these commands.

In short, the project will consist of three main parts:
  1. A main thread, accepting client connections (likely through sockets)
  2. A multi-threaded system for managing client interactions, including
  private messages and channels
  3. An IRC Message Parser, for communication with IRC clients.
