#!/bin/bash

#---------------------
#postgresql database options
#----------------------
#database name
if [ ! -z $1 ]
  then
      db=$1
  else
     if [ -r /usr/share/ostdlabs/scorecompass/scorecompass-api/conf/environment.properties ] ; then
        db=$(cat /usr/share/ostdlabs/scorecompass/scorecompass-api/conf/environment.properties | grep db.name | cut -d'=' -f 2)
    else
        db=scorecompass
    fi
fi

#user
if [ ! -z $2 ]
  then
     db_user=$2
  else
     if [ -r /usr/share/ostdlabs/scorecompass/scorecompass-api/conf/environment.properties ] ; then
         db_user=$(cat /usr/share/ostdlabs/scorecompass/scorecompass-api/conf/environment.properties | grep javax.persistence.jdbc.user | cut -d'=' -f 2)
     else
        db_user=scorecompass
     fi
fi

#password
if [ ! -z $3 ]
  then
     db_password=$3
  else
     if [ -r /usr/share/ostdlabs/scorecompass/scorecompass-api/conf/environment.properties ] ; then
        db_password=$(cat /usr/share/ostdlabs/scorecompass/scorecompass-api/conf/environment.properties | grep javax.persistence.jdbc.password | cut -d'=' -f 2)
    else
        db_password=scorecompass
    fi
fi


#host
if [ ! -z $4 ]
  then
     db_host=$4
  else
     if [ -r /usr/share/ostdlabs/scorecompass/scorecompass-api/conf/environment.properties ] ; then
      echo "DB Host environment file found"
      db_host=$(cat /usr/share/ostdlabs/scorecompass/scorecompass-api/conf/environment.properties | grep db.host | cut -d'=' -f 2)
    else
      db_host=localhost
    fi
fi


PGPASSWORD=$db_password
export PGPASSWORD

NOW=$(date +"%m-%d-%Y")
echo "Begin pgdump $NOW" >> /usr/share/ostdlabs/scorecompass/scorecompass-api/sql/backup/logs/dump-$NOW.log
pg_dump -d$db -h$db_host -U$db_user | gzip > "/usr/share/ostdlabs/scorecompass/scorecompass-api/sql/backup/backup-$NOW.sql.gz" 2>> /usr/share/ostdlabs/scorecompass/scorecompass-api/sql/backup/logs/dump-$NOW.log
echo "Finished pgdump $NOW" >> /usr/share/ostdlabs/scorecompass/scorecompass-api/sql/backup/logs/dump-$NOW.log

PGPASSWORD=
export PGPASSWORD
