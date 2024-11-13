const { defineConfig } = require('@vue/cli-service');

module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: 7000,
    proxy: {
      '/regis': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
      '/login': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
      '/start': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },'/cart': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },'/prizes': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },'/user': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
});
