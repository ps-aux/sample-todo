import React from 'react'
import autobind from 'react-autobind'
import RestClient from './restClient'

export default class TodoList extends React.Component {

    constructor(props) {
        super(props)
        autobind(this);
        this.state = {
            items: [],
            loading: true,
            editMode: false
        }
    }

    componentWillMount() {
        this.readItems()
    }

    /**
     * Error handler
     */
    handleProblem() {
        this.state.operationFailed = true
        this.readItems()
    }

    handleSuccess() {
        // NO-OP
    }

    readItems() {
        RestClient.findAllTodo(
            todos => {
                this.setState({items: todos, loading: false})
            },
            () => {
                console.log("Getting items failed")
                this.setState({operationFailed: true, items: [], loading: false})
            }
        )
    }


    createNew() {
        /**
         * Craeting new item implemented similar to updating existing one
         */
        const newTodo = {}
        const items = this.state.items
        items.push(newTodo)
        this.setState({items: items})

        this.edit(items.length - 1, true)
    }

    delete(index) {
        const items = this.state.items
        const todo = items.splice(index, 1)[0]

        RestClient.delete(todo, this.handleSuccess, this.handleProblem)
        this.setState({items: items})
    }

    edit(index, isNew = false) {
        const todo = this.state.items[index]
        this.editModeStarted = true
        this.creatingNew = isNew
        this.setState({
            editMode: true,
            editIndex: index,
            editVal: todo.text
        })
    }

    cancelEdit() {
        this.setState({
            editMode: false,
            editIndex: -1,
            items: this.state.items
        })
    }

    submitEdit() {
        const index = this.state.editIndex
        const todo = this.state.items[index]
        // Save it
        const oldVal = todo.text
        todo.text = this.state.editVal.trim()

        if (this.creatingNew) {
            RestClient.create(todo, created => {
                /** Insert the returned object which has links
                 and can be used in delete/udpate operations
                 No need to update the UI.
                 **/

                // Retrieve the current index (could have changed in the meantime)
                const freshIndex = this.state.items.indexOf(todo)
                this.state.items[freshIndex] = created

            }, this.handleProblem)
        } else {
            // Don't send the same text
            if (oldVal !== todo.text)
                RestClient.update(todo, this.handleSuccess, this.handleProblem)
        }

        const items = this.state.items
        this.setState({editMode: false, editIndex: -1, items: items})
    }


    componentDidUpdate() {
        if (this.state.editMode && this.editModeStarted) {
            const input = this.editedItemInput;
            input.focus()
            input.setSelectionRange(0, input.value.length)
            // So this is called just once - when edit mode started
            this.editModeStarted = false
        }

        this.state.operationFailed = false
    }


    render() {

        const editIndex = this.state.editIndex
        const editMode = this.state.editMode


        return (
            <div className="row">
                <div className="col-md-6 col-md-offset-3">
                    <button className="btn btn-primary"
                            title="Add new TODO item"
                            disabled={this.state.editMode}
                            onClick={this.createNew}>
                        <i className="fa fa-plus"></i> Add new
                    </button>
                    <hr/>

                    {this.renderMessages()}

                    <ul className="list-group">
                        {this.state.items && this.state.items.map((td, i) => {
                            return (editMode && i === editIndex) ? this.editMode(td, i) : this.readMode(td, i)
                        })
                        }
                    </ul>
                </div>
            </div>
        )
    }

    renderMessages() {
        //TODO omg refactor this if hell
        return (<div>
            {this.state.operationFailed &&
            <div className="alert alert-danger" role="alert">
                <strong>Sorry</strong> Operation did not succeed :(
            </div>}

            {!this.state.loading && !this.state.operationFailed &&
            this.state.items.length < 1 &&
            <div className="alert alert-success" role="alert">
                <strong>Well done!</strong> You have no pending tasks :)
            </div> }

            {this.state.loading && <strong> Retrieving data... </strong>}
        </div>)
    }

    readMode(todo, i) {
        return (<li key={i} className="list-group-item todo-item">

            <input type="text"
                   value={todo.text} disabled/>
            {this.state.editMode ||
            <i className="fa fa-trash pull-right todo-control"
               title="Delete" onClick={() => {this.delete(i)}}></i>
            }
            {this.state.editMode ||
            <i className="fa fa-pencil pull-right todo-control"
               title="Edit" onClick={() => {this.edit(i)}}></i> }
        </li>)
    }


    onKey(e) {
        if (e.key === 'Escape')
            this.cancelEdit()
        else if (e.key === 'Enter' && this.isEditValid())
            this.submitEdit()
    }

    isEditValid() {
        const val = this.state.editVal
        return val && val.length > 0
    }

    editMode(todo, i) {
        /**
         * This produces warning about changing uncontrolled input to controlled
         * when new item is added when the list is empty.
         *
         * It is strange as it is controlled component.
         * TODO investigate (out of ideas for now)
         */
        return (<li key={i}
                    className="list-group-item todo-item list-group-item-info">
            <input type="text"
                   value={this.state.editVal}
                   onKeyDown={this.onKey}
                   onChange={e => this.setState({editVal:e.target.value})}
                   placeholder="-- your task --"
                   ref={input => this.editedItemInput = input}/>

            <i className="fa fa-remove pull-right todo-control"
               title="Edit" onClick={this.cancelEdit}></i>

            {this.isEditValid() &&
            <i className="fa fa-check pull-right todo-control"
               title="Edit" onClick={this.submitEdit}></i> }
        </li>)
    }
}
