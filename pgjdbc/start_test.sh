CURRENTPATH="$PWD"
currentdate=$(date "+%Y%m%d%H%M%S")


# Intialization scripts
sudo apt update -y
sudo apt upgrade -y
sudo apt search golang-go
sudo apt search gccgo-go
sudo apt install golang-go -y

sudo apt install postgresql -y

# Log Path
logpath=$WIRESHARK_LOG_OUTPUT_DIR
if [ -z "${logpath}" ]; then
    cd ..
    logpath=${PWD}/Wireshark-Logs/pgx/$currentdate/
    cd $CURRENTPATH
else
    logpath=${WIRESHARK_LOG_OUTPUT_DIR}/Wireshark-Logs/pgx/$currentdate/
fi

# Check Log path exists
if [ ! -d "$logpath" ]; then
  mkdir -p $logpath
fi


PGPASSWORD=${PGXPASSWORD} psql -h ${PGXHOST} -U ${PGXUSER} -p ${PGXPORT} -f pgx_initializedb.sql

declare -a arr=("conn_test" "batch_test" "copy_from_test" "example_custom_type_test"
 "example_json_test" "large_objects_test" "pgbouncer_test" "query_test"
 "tx_test" "values_test" "internal/sanitize/sanitize_test" "pgxpool/conn_test" "pgxpool/pool_test" "pgxpool/tx_test" "stdlib/sql_test")

for i in "${arr[@]}"
do
        test_package="github.com/jackc/pgx/v4/"
        if [[ $i == *"/"* ]]; then
                folder="${i%/*}"
                echo $folder
                test_package="$test_package$folder"
        fi

        file_name="${i##*/}"

        if [ ! -d "$logpath$folder" ]; then
                          mkdir -p $logpath$folder
                  fi

                  tshark -i any  -f "host ${PGXHOST} && port ${PGXPORT}" -w ${logpath}${i}.pcapng &
                  sleep 5
                  echo $test_package
                  echo $file_name
                  PGX_TEST_DATABASE="host=${PGXHOST} user=${PGXUSER} port=${PGXPORT} database=pgx_test password=${PGXPASSWORD} sslmode=disable" go test ${test_package} -run ".*_"${file_name}"$" -v
                  pkill -f tshark
          done
