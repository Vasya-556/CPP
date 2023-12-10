using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace lab13
{
    internal class Coach
    {
        private object _id;

        [BsonId]
        public object Id
        {
            get
            {
                return _id is ObjectId objectId ? objectId : _id;
            }
            set
            {
                _id = value;
            }
        }

        [BsonElement("Name")]
        public string Name { get; set; }
        public Coach(string Name)
        {
            this.Id = ObjectId.GenerateNewId().ToString();
            this.Name = Name;
        }
    }
}
