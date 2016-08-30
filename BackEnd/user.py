import webapp2
from google.appengine.ext import ndb
import db
import json
import google.appengine.ext.db

#Directed to the user page
class User(webapp2.RequestHandler):
    
    
    def post(self):
        
        """
        Creates User Entity
        Variables:
        name - Required
        location - required - where they live
        job - required- what their job title is
        score - links the user to a happiness index score
        """

        #Checks for JSON
        if 'application/json' not in self.request.accept:
            self.response.status = 406
            self.response.status_message = "Error, this API requires a JSON type object"
            return
        
        #Creates a new user in the database
        newUser = db.User()
        username = self.request.get('username', default_value=None)
        password = self.request.get('password', default_value=None)
        name = self.request.get('name', default_value=None)
        location = self.request.get('location', default_value=None)
        job = self.request.get('job', default_value=None)
        score = self.request.get_all('scores[]', default_value=None)

        #Assigns the fields passed in through the POST request
        if name:
            newUser.name = name
        else:
            self.response.status = 400
            self.response.status_message = "Error, name required"
            
        if location:
            newUser.location = location

        if job:
            newUser.job = job
        
        if username:
            newUser.username = username
        
        if password:
            newUser.password = password
        
        #if a score is already provided, append that to the scores list
        if score:
            for i in score:
                newUser.scores.append(ndb.Key(db.Score, int(i)))            

        #pushes the entry to the database
        key = newUser.put()
        out = newUser.to_dict()
        self.response.write(json.dumps(out))
        return


    def get(self, **kwargs):
        
        check = self.request.get('check', default_value=None)
        uName = self.request.get('username', default_value=None)
        pWord = self.request.get('password', default_value=None)
        
        
        
        #Checks for JSON
        if 'application/json' not in self.request.accept:
            self.response.status = 406
            self.response.status_message = "Error, this API requires JSON objects"
            return
        
        #Writes the contents of that user id to the page
        if 'id' in kwargs:
            out = ndb.Key(db.User, int(kwargs['id'])).get().to_dict()
            self.response.write(json.dumps(out))
        
        if check:
            q = db.User.query(ndb.GenericProperty('password') == pWord)
            #q = db.User.filter(ndb.GenericProperty('username') == uName)
            result = q.get(keys_only=True)
            self.response.write(result.id())
            
        #otherwise, if no id is specified, all users will be listed
        else:
            q = db.User.query(ndb.GenericProperty('password') == pWord)
            #q = db.User.query(ndb.GenericProperty('username') == uName)
            result = q.get(keys_only=True)
            out = ndb.Key(db.User, result.id()).get().to_dict()
            self.response.write(json.dumps(out))

    def delete(self, **kwargs):
        #Checks for JSON
        if 'application/json' not in self.request.accept:
            self.response.status = 406
            self.response.status_message = "Error, this API requires JSON objects"
            return
        
        #If valid id is supplied, pull that id and delete the user
        if 'id' in kwargs:
            out = ndb.Key(db.User, int(kwargs['id'])).get()
            out.key.delete()

        #Otherwise, list of keys is shown on the page    
        else:
            q = db.User.query()
            keys = q.fetch(keys_only=True)
            results = {'keys': [x.id() for x in keys]}
            self.response.write(json.dumps(results)) 
            