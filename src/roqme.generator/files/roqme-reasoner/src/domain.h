//  This file belongs to the RoQME toolchain.
//  Copyright (C) 2019  University of Extremadura, University of Málaga, Biometric Vox.
//
//  RoQME is free software; you can redistribute it and/or modify
//  it under the terms of the GNU General Public License as published by
//  the Free Software Foundation; either version 3 of the License, or
//  any later version.
//
//  RoQME is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.
//
//  GNU GPLv3 link: http://www.gnu.org/licenses/gpl-3.0.html

#ifndef ROQME_DOMAIN_H
#define ROQME_DOMAIN_H

#include "variable.h"

#include <vector>
#include <unordered_map>
#include <memory>

namespace Roqme {

    class Domain {
    public:
        Domain();
        Domain(std::vector<const Variable*> scope);
        Domain(const Domain &domain);
        Domain(const Domain &domain, const std::unordered_map<unsigned,unsigned> &evidence);
        Domain(const Domain &d1, const Domain &d2);

        // Domain &operator=(const Domain &other);

        unsigned width() const { return _width; };
        unsigned size()  const { return _size;  };
        std::vector<const Variable*> scope() const { return _scope; };

        const Variable *operator[](unsigned i) const;
        unsigned operator[](const Variable* v) const;

        int index(const unsigned id);

        bool in_scope(const Variable* v) const;
        bool in_scope(unsigned id) const;

        void modify_scope(std::unordered_map<unsigned,const Variable*> modifier);

        void next_instantiation(std::vector<unsigned> &instantiation) const;
        void next_instantiation(std::vector<unsigned> &instantiation, const std::unordered_map<unsigned,unsigned> &evidence) const;
        void update_instantiation_with_evidence(std::vector<unsigned> &instantiation, const std::unordered_map<unsigned,unsigned> &evidence) const;

        unsigned position_instantiation(std::vector<unsigned> instantiation) const;
        unsigned position_consistent_instantiation(std::vector<unsigned> instantiation, const Domain &domain) const;
        unsigned position_consistent_instantiation(std::vector<unsigned> instantiation, const Domain &domain, const Variable *v, unsigned val) const;

        friend std::ostream &operator<<(std::ostream &o, const Domain &v);

    private:
        std::vector<const Variable*> _scope;
        std::vector<unsigned> _offset;
        unsigned _width;
        unsigned _size;
        std::unordered_map<unsigned, unsigned> _var_to_index;
    };

}

#endif
