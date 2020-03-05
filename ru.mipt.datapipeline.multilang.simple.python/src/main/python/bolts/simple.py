import os
from collections import Counter

from streamparse import TicklessBatchingBolt
from datetime import datetime as dt, timezone as tz
import json
from io import BytesIO
from tensorflow.keras.models import load_model

class SimplePythonBolt(TicklessBatchingBolt):
    outputs = ["predicts"]
    secs_between_batches = 5

    def initialize(self, conf, ctx):      
        self._model =  load_model('/apache-storm-2.1.0/models/simple-model.h5')

    def process_batch(self, key, tups):
        output = json.dumps([self._model.predict(json.loads(tup.values[1])).tolist() for tup in tups])
        self.emit([output])


SimplePythonBolt().run()
