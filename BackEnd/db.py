from google.appengine.ext import ndb

class Model(ndb.Model):
    def to_dict(self):
        d = super(Model, self).to_dict()
        d['key'] = self.key.id()
        return d

class Score(Model):
    happyIndex = ndb.StringProperty(required=True)
    date = ndb.StringProperty(required=True)
    username = ndb.StringProperty(required=True)
    password = ndb.StringProperty(required=True)
     
        
class User(Model):
    username = ndb.StringProperty(required=True)
    password = ndb.StringProperty(required=True)
    name = ndb.StringProperty(required=True)
    location = ndb.StringProperty(required=True)
    job = ndb.StringProperty(required=True)
    scores = ndb.KeyProperty(repeated=True)
    
    def to_dict(self):
        d = super(User, self).to_dict()
        d['scores'] = [s.id() for s in d['scores']]
        return d 