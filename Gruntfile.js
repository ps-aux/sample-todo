module.exports = grunt => {

    const srcDir = 'src/main/frontend/'
    const targetDir = 'target/classes/public/'
    const libDir = 'node_modules/'

    const jsDir = targetDir + 'js/'
    const fontsDir = targetDir + 'fonts/'
    const cssDir = targetDir + 'css/'

    grunt.initConfig({

        project: {},

        watch: {
            jade: {
                files: 'views/**',
                options: {
                    livereload: true,
                },
            },
            sass: {
                files: srcDir + 'style/**.scss',
                tasks: ['sass'],
                options: {
                    livereload: true,
                },
            },
            js: {
                files: srcDir + '**/*.js',
                tasks: ['browserify'],
                options: {
                    livereload: true,
                },
            },
            grunt: {
                files: 'Gruntfile.js',
                tasks: ['default'],
                options: {
                    livereload: true,
                }
            }
        },

        clean: [jsDir],

        browserify: {
            options: {
                transform: [
                    ['babelify', {presets: ['react', 'es2015']}],
                    'browserify-shim'],
                browserifyOptions: {
                    debug: true
                }
            },
            [jsDir + 'bundle.js']: srcDir + 'js/**'
        },
        sass: {
            options: {
                sourceMap: true,
                includePaths: [libDir]
            },
            dist: {
                files: {
                    [cssDir + 'style.css']: srcDir + 'style//main.scss'
                }
            }
        },

        copy: {
            fonts: {
                files: [{
                    expand: true,
                    cwd: './node_modules/font-awesome/fonts/',
                    src: '**/*',
                    dest: fontsDir,
                    filter: 'isFile'
                }]
            }
        }
    });

    require('load-grunt-tasks')(grunt)
    grunt.task.registerTask('default', ['clean', 'browserify', 'sass', 'copy'])

};