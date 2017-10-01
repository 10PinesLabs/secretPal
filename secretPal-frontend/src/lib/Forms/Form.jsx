import React from 'react';
import PropTypes from 'prop-types';
import R from 'ramda';

export class Form extends React.Component {
  constructor (props) {
    super(props);
    this.state = {};
  }

  getChildContext () {
    return {
      setInputState: (name, value, isValid) => {
        this.setState({[name]: {value, isValid}}, () => {
          if (R.all(R.prop('isValid'), R.values(this.state))) {
            this.props.onValid();
          } else {
            this.props.onInvalid();
          }
        });
      }
    };
  }

  render () {
    const props = R.omit(['validation', 'onValid'], this.props);

    return (
      <form
        {...props}
        onSubmit={(evt) => {
          evt.preventDefault();
          this.props.onSubmit(R.map(R.prop('value'), this.state));
        }}
      >
        {this.props.children}
      </form>
    );
  }
}

Form.propTypes = {
  onValid: PropTypes.func,
  onInvalid: PropTypes.func,
  onSubmit: PropTypes.func.isRequired,
  children: PropTypes.node
};

Form.childContextTypes = { setInputState: PropTypes.func.isRequired };
