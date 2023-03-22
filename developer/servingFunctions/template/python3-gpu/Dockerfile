FROM openfaas/classic-watchdog:0.18.1 as watchdog
FROM tensorflow/serving:latest-gpu as tfserving
#FROM python:3
# Allows you to add additional packages via build-arg
#ARG ADDITIONAL_PACKAGE

COPY --from=watchdog /fwatchdog /usr/bin/fwatchdog
RUN chmod +x /usr/bin/fwatchdog && \
    groupadd app && useradd -r -g app app
#RUN apt-get update \
#    && apt-get install -y ca-certificates ${ADDITIONAL_PACKAGE} \
#    && rm -rf /var/lib/apt/lists/

# Add non root user
#RUN groupadd app && useradd -r -g app app

WORKDIR /home/app/

COPY index.py .
#COPY requirements.txt   .

RUN mkdir -p /home/app/function && \
    chown -R app:app /home/app

USER app
#ENV PATH=$PATH:/home/app/.local/bin:/home/app/python/bin/
#ENV PYTHONPATH=$PYTHONPATH:/home/app/python

#RUN pip install -r requirements.txt -i https://pypi.tuna.tsinghua.edu.cn/simple --target=/home/app/python

#RUN mkdir -p function && mkdir -p models && \
#    touch ./function/__init__.py
COPY function function

#RUN pip3 install -r function/requirements.txt -i https://pypi.tuna.tsinghua.edu.cn/simple

USER root

RUN echo 'deb https://developer.download.nvidia.cn/compute/cuda/repos/ubuntu1804/x86_64 /' > /etc/apt/sources.list.d/cuda.list
RUN apt-key adv --recv-keys --keyserver keyserver.ubuntu.com A4B469963BF863CC
RUN echo "deb http://developer.download.nvidia.cn/compute/cuda/repos/ubuntu1804/x86_64 /" > /etc/apt/sources.list.d/cuda.list
RUN echo "deb http://developer.download.nvidia.cn/compute/machine-learning/repos/ubuntu1804/x86_64 /" > /etc/apt/sources.list.d/nvidia-ml.list
RUN apt-get update
RUN apt-get install -y python3
RUN apt-get install -y python3-pip

ENV PATH=$PATH:/usr/bin/python3:/usr/bin/pip3

RUN pip3 install -r function/requirements.txt -i https://pypi.tuna.tsinghua.edu.cn/simple

#COPY function function
#COPY models models
#ADD function/script/startup /usr/bin

RUN chown -R app:app /home/app/function \
    && chmod -R 777 /home/app/function \
    && mv /home/app/function/script/tf_serving_entrypoint.sh /usr/bin/tf_serving_entrypoint.sh 

USER app

ENV fprocess="python3 index.py"
EXPOSE 8080

HEALTHCHECK --interval=3s CMD [ -e /tmp/.lock ] || exit 1

#CMD ["fwatchdog"]
#CMD ["/home/app/function/script/startup"]
