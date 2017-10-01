import React from 'react';
import { shallow } from 'enzyme';
import { LoginForm } from '../../src/components/LoginPage/LoginForm';

describe('the login form', () => {
  describe('if I click submit button', () => {
    it('calls the login function', () => {
      const login = jest.fn();
      const wrapper = shallow(<LoginForm login={login}/>);

      wrapper.simulate('submit', {preventDefault: jest.fn()});
      expect(login).toBeCalledWith({username: '', password: ''});
    });
  });
  describe('when I input something', () => {
    it('updates its state', () => {
      const wrapper = shallow(<LoginForm login={jest.fn()}/>);

      wrapper.find('#password').simulate('change',
          {target: {value: 'password', id: 'password'}});
      expect(wrapper.state()).toHaveProperty('password', 'password');
    });
  });
});
