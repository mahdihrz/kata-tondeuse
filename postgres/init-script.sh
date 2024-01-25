#!/bin/bash
set -e
set -m

# Start PostgreSQL in the background
docker-entrypoint.sh postgres &

# Wait for PostgreSQL to start
until pg_isready -h localhost -U youruser; do
  sleep 1
done

# Execute SQL script
psql -h localhost -U dev_user -d db -f /postgres-init/init.sql

# Keep the container running
fg %1