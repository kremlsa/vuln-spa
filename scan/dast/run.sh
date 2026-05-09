docker run --rm -t \
  -v "$(pwd):/zap/wrk/:rw" \
  ghcr.io/zaproxy/zaproxy:stable zap.sh \
  -cmd \
  -autorun /zap/wrk/zap-auth-spa.yaml