# Four Corda Nodes Container

The container image installed four Corda nodes in a *single* container to simplify the usage. It is a cleaner way than running `Cordform(:deployNodes)` task and `runnodes.sh` script to start a local Corda test network.

## Project Structure

```text
.
├── Dockerfile
├── README.md
├── containerized-test
├── cordapps
│   ├── contracts-0.1.jar
│   └── workflows-0.1.jar
├── nodes
│   ├── Notary
│   │   └── node.conf
│   ├── PartyA
│   │   └── node.conf
│   ├── PartyB
│   │   └── node.conf
│   ├── PartyC
│   │   └── node.conf
│   └── runnodes.sh
└── scripts
    ├── docker-run.sh
    └── pull-nb.sh
```

* Dockerfile: the Dockerfile to build our image
* README.md: this file itself
* containerized-test: a Java Gradle project to test the image. The * program uses `TestContainers` to build the network on the fly * during testing and afterwards clean up the environment.
* cordapps: [a simple cordapp](https://github.com/corda/samples-java/tree/master/Features/customlogging-yocordapp) to be mounted to the * container during tests. Placed here for convenience.
* nodes: the **network bootstrapping context**
* scripts: Useful utility scripts
  * docker-run.sh: a script to run a docker container based on our image
  * pull-nb.sh: a script to download Corda's `network-bootstrapper`.

## The Docker Image

The image uses `ubuntu:20.04` as base. It then carries out the following steps:

1. Install Java 8
2. Copy the nodes directory (install the bootstrapped network)
3. Expose RPC ports and `ssh` ports
4. Define the entrypoint to start the network

In step 4, the started container will run the script defined [here](./nodes/runnodes.sh). This script iterate through all nodes. For each node, it first runs migration script (as we don't know in advance what cordapps will be installed) then start up the node. To keep the shell script running, we use the `tail -f` command, as suggested [here](https://stackoverflow.com/questions/30209776/docker-container-will-automatically-stop-after-docker-run-d)

## Build the Image

In the root directory:

1. Download network-bootstrapper: `./scripts/pull-nb.sh`
2. Bootstrap the network: `java -jar corda-network-bootstrapper-4.8.jar --dir=nodes`
3. Build the image: `docker build .`

## Image Usage

The original intent for building this image is to simplify dockerized integration tests using *TestContainers*. However, as said above, it can also serve to cleanly deploy test network.

For the latter usage, a [handy script](scripts/docker-run.sh) is provided. It takes care of mounting the cordapp volume and exposing necessary ports.

Note that the script uses two exported environment variables: `IMAGE_NAME` and `BASE_DIR` (the directory that contains your `cordapps` **directory**). When the above two variables are properly set, start up the container via `./scripts/docker-run.sh`

For the default cordapp (the *yo-cordapp*), you can interact with the container as such:

ssh to PartyA

```sh
ssh user1@localhost -p 10030
```

Issue a Yo to PartyB and exit

```sh
flow start YoFlow target: PartyB
```

ssh to PartyB

```sh
ssh user1@localhost -p 10031
```

Query the vault

```sh
run vaultQuery contractStateType: net.corda.samples.logging.states.YoState
```
