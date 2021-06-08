import { Component, Vue, Inject } from 'vue-property-decorator';

import { IDataDictionary } from '@/shared/model/settings/data-dictionary.model';
import DataDictionaryService from './data-dictionary.service';

@Component
export default class DataDictionaryDetails extends Vue {
  @Inject('dataDictionaryService') private dataDictionaryService: () => DataDictionaryService;
  public dataDictionary: IDataDictionary = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.dataDictionaryId) {
        vm.retrieveDataDictionary(to.params.dataDictionaryId);
      }
    });
  }

  public retrieveDataDictionary(dataDictionaryId) {
    this.dataDictionaryService()
      .find(dataDictionaryId)
      .subscribe(res => {
        this.dataDictionary = res.data;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
