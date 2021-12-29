#! /bin/bash

# source a setup script to make things easy

docker run -d \
-v $BASE_DIR/cordapps:/nodes/Notary/cordapps \
-v $BASE_DIR/cordapps:/nodes/PartyA/cordapps \
-v $BASE_DIR/cordapps:/nodes/PartyB/cordapps \
-v $BASE_DIR/cordapps:/nodes/PartyC/cordapps \
-p 10030:10030 \
-p 10031:10031 \
-p 10032:10032 \
-p 10033:10033 \
-p 10010:10010 \
-p 10011:10011 \
-p 10012:10012 \
-p 10013:10013 \
$IMAGE_NAME  # exported from parent shell