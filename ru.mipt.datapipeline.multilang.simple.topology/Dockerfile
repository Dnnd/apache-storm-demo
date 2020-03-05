FROM storm:2.1.0

RUN apt update && \
    apt install -y python3 python3-venv libpython3-dev build-essential unzip && \
    mkdir storm-venv && \
    python3 -m venv storm-venv/venv
COPY target/ru.mipt.datapipeline.multilang.simple.topology-1.0.0-SNAPSHOT.jar .
RUN unzip ru.mipt.datapipeline.multilang.simple.topology-1.0.0-SNAPSHOT.jar 'requirements/*' -d . && \ 
    storm-venv/venv/bin/pip install --upgrade pip && \
    find requirements -type f -name "*.txt" -exec storm-venv/venv/bin/pip install -r {} \;
COPY models models