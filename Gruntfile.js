module.exports = grunt => {

    const srcDir = 'src/main/frontend/'
    const targetDir = 'target/classes/public/'
    const libDir = 'node_modules/'

    const jsDir = targetDir + 'js/'
    const fontsDir = targetDir + 'fonts/'
    const cssDir = targetDir + 'css/'

    const isProd = process.env.NODE_ENV === 'production'

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
                    'babelify',
                ],
                browserifyOptions: {
                    debug: !isProd
                }
            },
            [jsDir + 'bundle.js']: srcDir + 'js/**'
        },

        uglify: {
            bundle: {
                files: {
                    [jsDir + 'bundle.js']: [jsDir + 'bundle.js']
                }
            },
        },

        cssmin: {
            style: {
                files: {
                    [cssDir + 'style.css']: [cssDir + 'style.css']
                }
            }
        },

        sass: {
            options: {
                sourceMap: !isProd,
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


    if (isProd) {
        grunt.task.registerTask('js', ['browserify', 'uglify'])
        grunt.task.registerTask('css', ['sass', 'cssmin'])
    } else {
        grunt.task.registerTask('js', ['browserify'])
        grunt.task.registerTask('css', ['sass'])
    }

    require('load-grunt-tasks')(grunt)
    grunt.task.registerTask('default', ['clean', 'js', 'css', 'copy'])

};