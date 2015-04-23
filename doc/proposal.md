MYRC
====

MYRC is to be a basic chat server, conforming to the IRC protocol outlined
in [RFC 2812](https://tools.ietf.org/html/rfc2812).  The program will manage
connected clients, and facilitate server-client and client-client interactions.

The project will consist of three main parts:
  1. A main thread, accepting client connections (likely through sockets)
  2. A multi-threaded system for managing client interactions
  3. An IRC Message Parser, for communication with IRC clients.
