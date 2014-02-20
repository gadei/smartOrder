using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Data.SQLite;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SmartOrderSystem.Database
{
    class GetFood
    {
        private static String database = "smartOrderDatabase.sqlite";

        public String getJsonMenu()
        {
            StringBuilder sb = new StringBuilder();
            StringWriter sw = new StringWriter(sb);
            JsonWriter jsonWriter = new JsonTextWriter(sw);

            try
            {
                SQLiteConnection connection = new SQLiteConnection();
                connection.ConnectionString = "Data Source=" + AppDomain.CurrentDomain.BaseDirectory + database;
                connection.Open();

                SQLiteCommand command = new SQLiteCommand(connection);
                command.CommandText = "SELECT * FROM drink";

                SQLiteDataReader reader = command.ExecuteReader();

                
                jsonWriter.WritePropertyName("drink");
                jsonWriter.WriteStartArray();
                while (reader.Read())
                {
                    int fieldcount = reader.FieldCount; // count how many columns are in the row
                    object[] values = new object[fieldcount]; // storage for column values
                    reader.GetValues(values); // extract the values in each column

                    jsonWriter.WriteStartObject();
                    for (int index = 0; index < fieldcount; index++)
                    { // iterate through all columns
                        jsonWriter.WritePropertyName(reader.GetName(index)); // column name
                        jsonWriter.WriteValue(values[index]); // value in column
                    }
                    jsonWriter.WriteEndObject();
                }
                jsonWriter.WriteEndArray();
                reader.Close();

                return sb.ToString();
            }
            catch (SQLiteException sqlException)
            {
                return null;
            }
        }

    }
}
