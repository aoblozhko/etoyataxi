#!/bin/bash

#function 

apply_script(){
  
  SELECT_SCRIPT="SELECT id,script FROM dbmigration WHERE script="
  INSERT_SCRIPT="INSERT INTO dbmigration (script,timestamp,username) VALUES "
  
  db_host=$1
  db=$2
  db_user=$3
  script=$4
  scriptname=$(basename $script)

  echo "----------------------"
  echo "* search $scriptname"
  query="$SELECT_SCRIPT'$scriptname';"
  info=$(psql -h$db_host -U$db_user -d$db -c"$query")

  if [ -z "`echo $info | grep -w $scriptname`" ]
    then	   
      echo "* execute script: $script"
      query="$INSERT_SCRIPT('$scriptname','$now','$USER');"
      psql -h$db_host -U$db_user -d$db -f $script >/dev/null 2>&1
      echo "* insert (\"$scriptname\",\"$now\",\"$USER\") into table"
      psql -h$db_host -U$db_user -d$db -c"$query" >/dev/null 2>&1     
 fi
}

#Path to the folder with mysql scripts 
scripts=`dirname $0`/../migration/


echo "
    *usage: dbmigraton_alt.sh [database] [user] [password] [host]
              
    *defaults: database = etoyataxi
               user =  etoyataxi
               password = etoyataxi
               host=localhost
"
echo  "
   *description:
         1. create table dbmigration (id, script, date, username) if it doesn't exist
         2. search scripts from $scripts folder in dbmigration table 
         3. if script doesn't exist in table they are applied 
         4. information about applied script (name,date,user) is written to dbmigration table
"
echo ""
echo "
   *Execution:"


#---------------------
#postgresql database options
#----------------------
#database name
if [ ! -z $1 ]
  then
      db=$1
  else
     if [ -r /usr/share/ostdlabs/etoyataxi/etoyataxi-api/conf/environment.properties ] ; then
        db=$(cat /usr/share/ostdlabs/etoyataxi/etoyataxi-api/conf/environment.properties | grep db.name | cut -d'=' -f 2)
    else
        db=etoyataxi
    fi
fi

#user
if [ ! -z $2 ]
  then
     db_user=$2
  else
     if [ -r /usr/share/ostdlabs/etoyataxi/etoyataxi-api/conf/environment.properties ] ; then
         db_user=$(cat /usr/share/ostdlabs/etoyataxi/etoyataxi-api/conf/environment.properties | grep javax.persistence.jdbc.user | cut -d'=' -f 2)
     else
        db_user=etoyataxi
     fi
fi

#password
if [ ! -z $3 ]
  then
     db_password=$3
  else
    if [ -r /usr/share/ostdlabs/etoyataxi/etoyataxi-api/conf/environment.properties ] ; then
        db_password=$(cat /usr/share/ostdlabs/etoyataxi/etoyataxi-api/conf/environment.properties | grep javax.persistence.jdbc.password | cut -d'=' -f 2)
    else
        db_password=etoyataxi
    fi
fi

#host
if [ ! -z $4 ]
  then
     db_host=$4
  else
    if [ -r /usr/share/ostdlabs/etoyataxi/etoyataxi-api/conf/environment.properties ] ; then
      echo "DB Host environment file found"
      db_host=$(cat /usr/share/ostdlabs/etoyataxi/etoyataxi-api/conf/environment.properties | grep db.host | cut -d'=' -f 2)
    else
      db_host=localhost
    fi
fi

#------------------------
#Queries
#-----------------------
CREATE_TABLE="CREATE TABLE dbmigration (id serial, script text, timestamp timestamp, username text,  PRIMARY KEY (id))"
SELECT_SCRIPT="SELECT id,script FROM dbmigration WHERE script="
INSERT_SCRIPT="INSERT INTO dbmigration (script,timestamp,user) VALUES "

#temprorary folder
tmpdir=/tmp


#current date
now="$(date +"%Y-%m-%d")"


#password fo postgresql
PGPASSWORD=$db_password
export PGPASSWORD


DBEXIST=`psql -l -h$db_host -U$db_user  2>&1`

if [ ! -z "`echo $DBEXIST | grep -o "$db_host"`" ];
then
echo "ERROR! COULDN'T CONNECT TO $db_host host! CHECK NETWORK CONNECTION!"
exit 2
fi


if [ ! -z "`echo $DBEXIST | grep -o "password authentication failed"`" ];
then
echo "ERROR! USER $db_user DOESN\`T EXISTS OR PASSWORD IS INCORRECT!"
exit 2
fi

if [ -z "`echo $DBEXIST | grep -w $db`" ];
then
echo "ERROR! DATABASE $db DOESN\`T EXISTS!"
exit 2
fi

#---------------------------------------------------------------------------------
if [ -z "`psql -U$db_user -h$db_host -d$db -c \"select * from pg_tables where schemaname='public' and tablename='dbmigration'\" | grep dbmigration `" ]

 then
     echo "1. Create \"dbmigration\" table"
     psql -U$db_user -h$db_host -d$db -c"$CREATE_TABLE" >/dev/null 2>&1
fi


#--------------------------------------------------------------------------------
cd $scripts
echo "2. search sql scripts from $scripts folder in database: " 
for script in migration*; do
  if [ ${script##*.} = "sql" ] 
    then
     apply_script $db_host $db $db_user $script
  fi     
  if [ ${script##*.} = "jar" ] 
    then
    echo $script
     foldername=$(basename $script)
     foldername="${foldername%%.*}"
     unzip -d $tmpdir/$foldername $script 
     for scriptInArch in  $tmpdir/$foldername/*.sql; do
         apply_script $db_host $db $db_user $db_password $scriptInArch
     done
     rm -rf $tmpdir/$foldername
  fi     
done


#reset passsword fo postgresql
PGPASSWORD=
export PGPASSWORD
