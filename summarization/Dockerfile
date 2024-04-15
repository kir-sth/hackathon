FROM python:3.12


COPY requirements.txt /workdir/summarization/
COPY app.py config_reader.py /workdir/summarization/
COPY /ml/ /workdir/summarization/ml/

WORKDIR /workdir

RUN pip install -r summarization/requirements.txt

CMD ["python", "summarization/app.py"]