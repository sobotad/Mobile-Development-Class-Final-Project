import webapp2
from google.appengine.ext import ndb
import db
import json
import google.appengine.ext.db

#Directed to the score page
class Score(webapp2.RequestHandler):
    
    def post(self):
        
        """
        Creates User Entity
        Variables:
        happyIndex - required - user's happiness score
        activties - required - list of activities they did (This might be added in as another component in the future, making activities their own entities)
        date - What date the score is associated with
        """

        #Checks for JSON
        if 'application/json' not in self.request.accept:
            self.response.status = 406
            self.response.status_message = "Error, this API requires a JSON type object"
            return
        
        #Creates a new score entry in the database
        newScore = db.Score()
        happyIndex = self.request.get('happyIndex', default_value=None)
        date = self.request.get('date', default_value=None)
        username = self.request.get('username', default_value=None)
        password = self.request.get('password', default_value=None)

        if happyIndex:
            newScore.happyIndex = happyIndex
        else:
            self.response.status = 400
            self.response.status_message = "Error, score required"
           
            
        if date:
            newScore.date = date
            
        if username:
            newScore.username = username
        
        if password:
            newScore.password = password

        #Creates the entry 
        key = newScore.put()
        out = newScore.to_dict()
        self.response.write(json.dumps(out))
        return

    def get(self, **kwargs):
        
        uName = self.request.get('username', default_value=None)
        pWord = self.request.get('password', default_value=None)
        
        #Checks for JSON
        if 'application/json' not in self.request.accept:
            self.response.status = 406
            self.response.status_message = "Error, this API requires JSON objects"
            return
        
        #Displays the contents of that score if an id is passed
        if 'id' in kwargs:
            out = ndb.Key(db.Score, int(kwargs['id'])).get().to_dict()
            self.response.write(json.dumps(out))
        
        if uName:
            q = db.Score.query(ndb.GenericProperty('password') == pWord)
            #q = db.Score.query(ndb.GenericProperty('username') == uName)
            result = q.get(keys_only=True)
            self.response.write(result.id())
            
        #Otherwise just displays the keys for all the scores
        else:
            q = db.Score.query()
            keys = q.fetch(keys_only=True)
            results = {'keys': [x.id() for x in keys]}
            self.response.write(json.dumps(results))

    def delete(self, **kwargs):
        #Checks for JSON
        if 'application/json' not in self.request.accept:
            self.response.status = 406
            self.response.status_message = "Error, this API requires JSON objects"
            return
        
        #If a valid id is passed, the score is deleted
        if 'id' in kwargs:
            out = ndb.Key(db.Score, int(kwargs['id'])).get()
            out.key.delete()
            #Queries the users, in order to search for associated scores
            q = db.User.query()
            users = q.fetch()
            
            #Loops through the users
            for user in users:
                if user.scores != None:
                    #Each user can support multiple scores, so it loops through to check them all
                    for score in user.scores:
                        
                        #If the score id that was deleted, was found in the user.scores, that score key is removed from the list of scores, and the user is updated
                        if (int(kwargs['id']) == score.id()):
                            delScore = ndb.Key(db.User, user.key.id()).get()
                            delScore.scores.remove(score)
                            delScore.put()

        #Otherwise, just displays the list of available, valid keys
        else:
            q = db.Score.query()
            keys = q.fetch(keys_only=True)
            results = {'keys': [x.id() for x in keys]}
            self.response.write(json.dumps(results))
   
#This class is for adding a score to a user   
class addScore(webapp2.RequestHandler):
    
    def put(self, **kwargs):
        
        #Checks for JSON
        if 'application/json' not in self.request.accept:
            self.response.status = 406
            self.response.status_message = "Air Or.  Needs JSON."

        #Creates a link to the user in the database, using the id provided
        if 'uid' in kwargs:
            user = ndb.Key(db.User, int(kwargs['uid'])).get()
            if not user:
                self.response.status = 404
                self.response.status_message = "Error: User not found"
             
        #References the appropriate score in the database, using the id provided
        if 'sid' in kwargs:
            score = ndb.Key(db.Score, int(kwargs['sid']))
            if not score:
                self.response.status = 404
                self.response.status_message = "Error: Score not found"
                
        #If the score is not currently associated with the current user, that score key id will be appended to the list of keys for that user
        if score not in user.scores:
            user.scores.append(score)
            user.put()
        
        self.response.write(json.dumps(user.to_dict()))            