#服务名称
IMAGE_NAME = csp-be
#发布到哪个Project，默认platform
NS ?= cw-platform
#helm路径
HELM_CHART_DIR = ./start/helms

#设置devops-kit目录
ROOTPATH = ../devops-kit

ADDR_DEV = 36b3efac-b81c-49fa-bd82-6d7140a812f2
ADDR_TEST=52acf9b6-c392-479a-9533-1f1c6e387ebf
ADDR_PROD=b3243f8e-5637-43a8-ae12-6f0dadf30d5d

include ${ROOTPATH}/hacks/build-java-lib.mk

.DEFAULT_GOAL = all
.PHONY: all build image-base push rancher-deploy

all: build image push rancher-deploy

build:java-linux-bin
image:image-base
push: push-base
rancher-deploy: helm-deploy-base