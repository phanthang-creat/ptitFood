FROM ubuntu:latest
LABEL authors="phanthang"

ENTRYPOINT ["top", "-b"]