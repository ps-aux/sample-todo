//We need this to translate react templates here
import ReactDOM from 'react-dom'
import React from 'react'
import TodoList from './todoList'
import RestClient from './restClient'
import 'whatwg-fetch'

//Init boostrap
require('bootstrap')

const token = document.querySelector("meta[name=token]").getAttribute("content")

RestClient.setErrorHandler((e => console.error(e)))
RestClient.setToken(token)

reactStart()

function reactStart() {

    ReactDOM.render((
        <div className="container">
            <TodoList/>
        </div>
    ), document.getElementById('app'))
}


