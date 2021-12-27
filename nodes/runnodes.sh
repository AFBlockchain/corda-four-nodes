#! /bin/bash

nodes=('Notary' 'PartyA' 'PartyB' 'PartyC')
for node in "${nodes[@]}"; do
    cd $node
    java -jar corda.jar --no-local-shell &
    cd ..
done

# use `pkill -f corda.jar` to quickly shutdown all nodes