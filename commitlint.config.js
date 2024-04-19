// https://commitlint.js.org/#/reference-configuration?id=javascript
const Configuration = {
  extends: ["@commitlint/config-conventional"],
  rules: {
    // https://commitlint.js.org/#/reference-rules
    "scope-enum": [
      2,
      "always",
      ["elastic", "kafka", "mqtt", "pulsar", "redash", "tick", "timescaledb"],
    ],
  },
};

module.exports = Configuration;
