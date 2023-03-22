# FROM teamserverless/license-check:0.3.6 as license-check

FROM golang:1.19 as build
ENV CGO_ENABLED=0
ENV GO111MODULE=on
ENV GOFLAGS=-mod=vendor

# COPY --from=license-check /license-check /usr/bin/

RUN mkdir -p /go/src/github.com/openfaas/faas-netes
WORKDIR /go/src/github.com/openfaas/faas-netes
COPY . .

# RUN license-check -path /go/src/github.com/openfaas/faas-netes/ --verbose=false "Alex Ellis" "OpenFaaS Author(s)"
# RUN gofmt -l -d $(find . -type f -name '*.go' -not -path "./vendor/*")
# RUN go test -v ./...

RUN GOOS=linux go build \
        --ldflags "-s -w" \
        -a -installsuffix cgo -o faas-netes .

FROM alpine:3.17 as ship

LABEL org.label-schema.license="MIT" \
      org.label-schema.vcs-url="https://github.com/openfaas/faas-netes" \
      org.label-schema.vcs-type="Git" \
      org.label-schema.name="openfaas/faas-netes" \
      org.label-schema.vendor="openfaas" \
      org.label-schema.docker.schema-version="1.0"

RUN echo -e http://mirrors.ustc.edu.cn/alpine/v3.11/main/ > /etc/apk/repositories

RUN addgroup -S app \
    && adduser -S -g app app \
    && apk --no-cache add \
    ca-certificates

WORKDIR /home/app

EXPOSE 8080

ENV http_proxy      ""
ENV https_proxy     ""

COPY --from=build /go/src/github.com/openfaas/faas-netes/faas-netes    .

RUN chown -R app:app ./

USER app

CMD ["./faas-netes"]
