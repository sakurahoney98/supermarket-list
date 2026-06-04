#!/bin/sh

set -e

COMPOSE_FILE="compose.server.yaml"

show_help() {
    echo "Uso:"
    echo "  ./docker-run.sh --server"
    echo "  ./docker-run.sh --clean"
    echo "  ./docker-run.sh --reset-db"
    exit 1
}

if [ $# -ne 1 ]; then
    show_help
fi

case "$1" in

    --server)
        echo "Parando containers..."
        docker compose -f ${COMPOSE_FILE} down

        echo "Subindo ambiente..."
        docker compose -f ${COMPOSE_FILE} up -d --build

        echo "Ambiente iniciado."
        ;;

    --clean)
        echo "Parando containers..."
        docker compose -f ${COMPOSE_FILE} down

        echo "Removendo imagens..."
        docker rmi supermarket-backend:latest || true
        docker rmi supermarket-frontend:latest || true

        echo "Reconstruindo ambiente..."
        docker compose -f ${COMPOSE_FILE} up -d --build

        echo "Ambiente recriado."
        ;;

    --reset-db)
        echo "ATENÇÃO: removendo volumes e banco."

        docker compose -f ${COMPOSE_FILE} down -v

        docker compose -f ${COMPOSE_FILE} up -d --build

        echo "Banco recriado."
        ;;

    *)
        show_help
        ;;
esac

exit 0
