#! /bin/bash

nodes=('Notary' 'PartyA' 'PartyB' 'PartyC')
for node in "${nodes[@]}"; do
    cd $node
    # Always run migration code - you don't know which cordapp will change the schema
    java -jar corda.jar run-migration-scripts --core-schemas --app-schemas; java -jar corda.jar --no-local-shell &
    cd ..
done

# use `pkill -f corda.jar` to quickly shutdown all nodes

# keep the process running
tail -f /dev/null