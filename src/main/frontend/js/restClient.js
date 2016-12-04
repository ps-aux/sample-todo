class RestClient {

    constructor() {
        this.todoPath = 'api/todo'
    }

    setToken(token) {
        this.token = token;
    }

    /**
     * Global error which will be used when specific ones are not provided
     */
    setErrorHandler(handler) {
        this.errorHandler = handler
    }

    doFetch(method, url, data) {
        if (!this.token)
            throw 'No token set'

        const headers = {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }
        headers['Authorization'] = this.token
        const config = {method: method, headers}

        if (method !== 'GET' && method !== 'HEAD') {
            config.body = JSON.stringify(data)
        }

        return fetch(url, config)
    }


    findAllTodo(done, err) {
        // Spring wraps collection resources to 'content'
        this.doFetch('GET', this.todoPath)
            .then(res => this.process(res, 200,
                d => {
                    const content = d.content

                    // Content contains one value - symbolic empty collection
                    if (content[0].collectionValue)
                        done([])
                    else
                        done(d.content)
                }, err)).catch(err)
    }

    create(todo, done, err) {
        this.doFetch('POST', this.todoPath, todo)
            .then(res => this.process(res, 201, done, err))
            .catch(err)
    }

    update(todo, done, err) {
        this.doFetch('PUT', this._findSelfLink(todo), todo)
            .then(res => this.process(res, 200, done, err))
            .catch(err)
    }

    delete(todo, done, err) {
        this.doFetch('DELETE', this._findSelfLink(todo))
            .then(res => {
                this.process(res, 204, done, err, true)
            })
            .catch(err)
    }

    process(res, status, done, errHandler, ignoreData = false) {
        if (res.status !== status) {
            (errHandler || this.errorHandler)('Non ' + status + ' return code', res)
        } else {
            if (ignoreData)
                done()
            else
                res.json().then(done)
        }
    }

    _findSelfLink(todo) {
        return todo.links.filter(l => l.rel = 'self')[0].href
    }

}

export default new RestClient()
