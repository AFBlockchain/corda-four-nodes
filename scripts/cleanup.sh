#! /bin/bash

# clean up the ./nodes/ folder
# as generated by the bootstrapper
# reference: https://unix.stackexchange.com/questions/153862/remove-all-files-directories-except-for-one-file

cd ../nodes
nodes=('Notary' 'PartyA' 'PartyB' 'PartyC')
for node in "${nodes[@]}"; do
    cd $node
    find . ! -name 'node.conf' -type f -exec rm -r {} + # regular files
    find . ! -name 'node.conf' -type d -exec rm -rf {} + # directories
    cd ..
done