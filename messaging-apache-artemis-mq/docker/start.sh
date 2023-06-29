set -e

if ! [ -f "${BROKER_HOME}"/etc/broker.xml ]; then
   "$APP_HOME"/bin/artemis create --user "${APP_USER}" --password "${APP_PASSWORD}" \
               --silent \
               "${LOGIN_OPTION}" \
               --http-host "${HTTP_HOST}" \
               "${EXTRA_ARGS}" \
               "${BROKER_HOME}"
else
  echo "broker already created, ignoring creation"
fi

exec  "${BROKER_HOME}"/bin/artemis run
