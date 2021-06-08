import PositionComponent from './position.vue';
import PositionCompactComponent from './position-compact.vue';
import PositionUpdate from './position-update.vue';

const Position = {
  install: function (Vue) {
    Vue.component('jhi-position', PositionComponent);
    Vue.component('jhi-position-compact', PositionCompactComponent);
    Vue.component('jhi-position-update', PositionUpdate);
  },
};

export default Position;
