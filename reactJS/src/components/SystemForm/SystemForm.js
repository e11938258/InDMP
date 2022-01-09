import { Component } from "react";
import "./SystemForm.css";

class SystemForm extends Component {
  constructor(props) {
    super(props);

    this.handleInputChange = this.handleInputChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.clearInputValues = this.clearInputValues.bind(this);

    this.clearInputValues();
  }

  clearInputValues() {
    this.state = {
      name: "",
      host: "",
      dmp_endpoint: "",
      type: "",
    };
  }

  handleInputChange(event) {
    const target = event.target;
    const value = target.value;
    const name = target.name;

    this.setState({
      [name]: value,
    });
  }

  handleSubmit(event) {
    event.preventDefault();

    // Create data
    const system = {
      name: this.state.name,
      host: this.state.host,
      dmp_endpoint: this.state.dmp_endpoint,
      type: this.state.type,
    };

    // Create a request
    const requestOptions = {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(system),
    };

    // Fetch the request
    fetch("http://localhost:8080/system", requestOptions).then(() => {
      window.location.reload();
    });
  }

  render() {
    return (
      <div>
        <h3>Add a new system</h3>
        <form onSubmit={this.handleSubmit}>
          <label>
            Name:
            <input
              type="text"
              name="name"
              value={this.state.name}
              onChange={this.handleInputChange}
            />
          </label>
          <label>
            Host:
            <input
              type="text"
              name="host"
              value={this.state.host}
              onChange={this.handleInputChange}
            />
          </label>
          <label>
            Endpoint for new DMPs:
            <input
              type="text"
              name="dmp_endpoint"
              value={this.state.dmp_endpoint}
              onChange={this.handleInputChange}
            />
          </label>
          <label>
            Type:
            <select
              name="type"
              value={this.state.type}
              onChange={this.handleInputChange}
            >
              <option value="">Select type</option>
              <option value="DMP_APP">DMP Application</option>
              <option value="REPOSITORY_STORE">Repository store</option>
              <option value="ADMINISTRATIVE_DATA_COLLECTOR">
                Administrative data collector
              </option>
              <option value="FUNDER_SYSTEM">Funder system</option>
              <option value="IT_RESOURCE">IT resource</option>
              <option value="REPOSITORY_INGESTOR">Repository ingestor</option>
            </select>
          </label>
          <input type="submit" className="button" value="Add" />
        </form>
      </div>
    );
  }
}

export default SystemForm;
