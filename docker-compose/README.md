This repository contains links to OpenBPM Control resources and supporting configuration files for running Docker Compose as a local development option.

## Using Docker Compose

The following Docker Compose files are provided:
1. [docker-compose-core.yaml](docker-compose-core.yaml) which contains these components:
    - **OpenBPM Control**
    - PostgresQL which is used by **OpenBPM Control**

   **Note:** This Docker Compose is suitable for cases when you need to connect to already running external Camunda 7 engines.
2. [docker-compose-full.yaml](docker-compose-core.yaml) which contains these components:
    - **OpenBPM Control**
    - PostgresQL which is used by **OpenBPM Control**
    - Camunda 7 as the external BPM engine - an engine that runs on the `8082` port by default.
    - PostgresQL which is used by Camunda 7

## Running using Docker Compose

1. Clone this repository
2. Open a terminal 
3. Go to the `docker-compose` directory:
   ```shell 
   cd docker-compose
   ```
4. Execute the following commands:
     - **OpenBPM Control only:** If you want to run OpenBPM Control without the Camunda 7 engine, use:
        ```shell
        docker compose -f docker-compose-core.yaml up -d
        ```
        To check container statuses:
        ```shell
        docker container ls -f "name=openbpm-control-app" -f "name=openbpm-control-database"
        ```
    - **OpenBPM Control + external Camunda 7:** If you want to run not only OpenBPM Control, but also the Camunda 7 engine, use
      ```shell
      docker compose -f docker-compose-full.yaml up -d
      ```
      To check container statuses:
      ```shell
      docker container ls -f "name=openbpm-control-app" -f "name=openbpm-control-database" -f "name=camunda7-bpm-platform" -f "name=camunda7-bpm-platform-database"
      ```
5. Open OpenBPM Control in your browser using the link [http://localhost:8081](http://localhost:8081) and login as `admin/admin`.
6. Configure your first connection to the Camunda 7 engine. 
   
   If you are running Camunda 7 using `docker-compose-full.yaml`, enter the following data:
   - **Name**: any short name for the configuring engine, e.g. `Dev stand`
   - **Base URL**: `http://camunda7-bpm-platform:8082/engine-rest`
   - **Authentication**: Enabled
   - **Authentication type**: Basic
   - **Username:** `admin`
   - **Password:** `admin`
7. Click the **Test connection** to check that values are correct and the BPM engine is available.
8. Save the Camunda 7 connection configuration.

Now you can deploy processes to the configured BPM engine and manage process instances running on this engine.

## Additional configuration
You can explore the environment variables used by services in the `.env` file and change them to run locally if necessary.