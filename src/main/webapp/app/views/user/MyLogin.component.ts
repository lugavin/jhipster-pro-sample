import axios from 'axios';
import { email, maxLength, minLength, required } from 'vuelidate/lib/validators';
import Component from 'vue-class-component';
import { Vue, Inject } from 'vue-property-decorator';
import AccountService from '@/account/account.service';
import { timeFix } from '@/utils/util';

function handleUsernameOrEmail(value) {
  if (typeof value === 'undefined' || value === null || value === '') {
    return true;
  }
  const regex = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;
  return regex.test(value);
}
const validations: any = {
  username: {
    required,
    handleUsernameOrEmail,
  },
  password: {
    required,
  },
};
@Component({
  validations,
})
export default class LoginForm extends Vue {
  @Inject('accountService')
  private accountService: () => AccountService;
  public authenticationError = null;
  public username = null;
  public password = null;
  public rememberMe: boolean = null;
  customActiveKey = 'tab1';
  loginBtn = false;
  // login type: 0 email, 1 username, 2 telephone
  loginType = 0;
  isLoginError = false;
  requiredTwoStepCaptcha = false;
  stepCaptchaVisible = false;
  state = {
    time: 60,
    loginBtn: false,
    // login type: 0 email, 1 username, 2 telephone
    loginType: 0,
    smsSendBtn: false,
  };
  public doLogin(): void {
    const data = { username: this.username, password: this.password, rememberMe: this.rememberMe };
    axios
      .post('api/authenticate', data)
      .then(result => {
        const bearerToken = result.headers.authorization;
        if (bearerToken && bearerToken.slice(0, 7) === 'Bearer ') {
          const jwt = bearerToken.slice(7, bearerToken.length);
          if (this.rememberMe) {
            localStorage.setItem('jhi-authenticationToken', jwt);
          } else {
            sessionStorage.setItem('jhi-authenticationToken', jwt);
          }
        }
        this.authenticationError = false;
        // this.$root.$emit('bv::hide::modal', 'login-page');
        this.accountService().retrieveAccount();
      })
      .catch(() => {
        console.log('authenticationError');
        this.authenticationError = true;
      });
  }
}
