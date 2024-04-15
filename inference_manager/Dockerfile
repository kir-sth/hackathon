FROM python:3.12


COPY requirements.txt /workdir/inference_manager/
COPY main.py mock_data.json /workdir/inference_manager/

WORKDIR /workdir

RUN pip install -r inference_manager/requirements.txt

CMD ["python", "inference_manager/main.py"]